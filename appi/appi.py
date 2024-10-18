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

@app.get("/vote/")
def get_vote():
    return Vote

@app.post("/vote/")
def create_vote(vote: Vote):
    return vote

@app.get("/vote/{vote_id}")
def read_vote(vote_id: int):
    return {"Vote": vote_id, "Code": Vote.code, "Vote": Vote.vote}

@app.put("/vote/{vote_id}")
def update_item(vote_id: int, vote: Vote):
    return {"Vote_id": vote_id, "Code": Vote.code}