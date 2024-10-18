from typing import Union

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

vote = []
class Vote(BaseModel):
    code: int
    vote: bool



@app.get("/")
def read_root():
    return {"Welcome": "Welcome to Studention API"}

@app.get("/vote")
def get_vote():
    return Vote

@app.get("/vote/{vote_id}")
def read_vote(vote_id: int):
    return {"Vote": vote_id, "Code": Vote.code}

@app.put("/vote/{vote_id}")
def update_item(vote:Vote):
    return {"Message": f"Voto {Vote.code} insertado"}