from random import randint
from typing import Optional
from fastapi import FastAPI
from pydantic import BaseModel





app = FastAPI()

votes = [{
"id": "FPTSP071",
"boton1": 0,
"boton2": 0
}]
class Vote(BaseModel):
    id : str 
    boton1: int
    boton2: int


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

@app.put("/votes/{vote_id}")
def update_vote1(vote_id: str):
    for vote in votes:
        if vote["id"] == vote_id:
            vote["boton1"] += 1
            return vote
    return {"message": "Vote not found"}



@app.put("/votes/{vote_id}")
def update_vote2(vote_id: str):
    for vote in votes:
        if vote["id"] == vote_id:
            vote["boton2"] += 1
            return vote
    return {"message": "Vote not found"}

