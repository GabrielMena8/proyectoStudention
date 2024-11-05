from random import randint
from typing import Optional
from fastapi import FastAPI
from pydantic import BaseModel
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate("app/src/main/java/com/database/database/studention-11f13-firebase-adminsdk-4fnmw-16073fda31.json")
firebase_admin.initialize_app(cred)

db = firestore.client()


app = FastAPI()

votes = [{
"id": "FPTSP071",
"boton1": 0,
"boton2": 0
}]

class Vote(BaseModel): 
    id: str
    boton1: int
    boton2: int

def update_firebase(vote): 
    doc_ref = db.collection('voto').document(vote["id"])
    doc_ref.set(vote)

def test_firestore(): 
    try: 
        doc_ref = db.collection('voto').document('FPTSP071') 
        doc_ref.set({ 'boton1': 1, 'boton2': 2 }) 
        print("Firestore update test complete") 
    except Exception as e: print(f"Error: {e}")
test_firestore()

@app.get("/")
def read_root():
    return {"Welcome": "Welcome to Studention API"}

@app.get("/votes")
def get_votes():
    return votes

@app.post("/votes")
def save_vote(vote: Vote):
    vote.id = len(votes) + 1
    votes.append(vote.model_dump())
    return votes[-1]

@app.get("/votes/{vote_id}")
def get_vote(vote_id: int):
    for vote in votes:
        if vote["id"] == vote_id:
            return vote
    return {"message": "Vote not found"}

@app.put("/votes/{vote_id}/boton1") 
async def update_vote1(vote_id: str): 
    for vote in votes: 
        if vote["id"] == vote_id:
            vote["boton1"] += 1 
            update_firebase(vote) 
            return vote 
        return {"message": "Vote not found"}

@app.put("/votes/{vote_id}/boton2") 
async def update_vote2(vote_id: str): 
    for vote in votes:
        if vote["id"] == vote_id:   
            vote["boton2"] += 1
            update_firebase(vote)  
            return vote 
        return {"message": "Vote not found"}

