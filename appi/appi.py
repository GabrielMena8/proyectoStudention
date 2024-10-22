from random import randint
from typing import Optional
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

votes = [{
"id": 1,
"code": "53273404",
"boton1": True,
"boton2": False
}]
class Vote(BaseModel):
    id : Optional[int] 
    code: Optional[str] 
    boton1: bool
    boton2: bool

def random_code():
  random_list = []

  for i in range(8):
    x = randint(0,9) 
    random_list.append(str(x)) 
  
  code = ''.join(random_list) 
  
  return code  

@app.get("/")
def read_root():
    return {"Welcome": "Welcome to Studention API"}

@app.get("/votes")
def get_votes():
    return votes

@app.post("/votes")
def save_vote(vote: Vote):
    vote.code = random_code()
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
def update_vote(vote_id: int, updateVote: Vote):
    for vote in votes:
        if vote["id"] == vote_id:
            vote["boton1"] = updateVote.boton1
            vote["boton2"] = updateVote.boton2
            return vote
    return {"message": "Vote not found"}