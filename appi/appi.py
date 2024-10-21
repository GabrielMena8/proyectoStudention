from random import randint
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

vote = []
class Vote(BaseModel):
    code: str = None
    boton1: str
    boton2: str

def random_code():
  random_list = []

  for i in range(8):
    x = randint(0,9) 
    random_list.append(str(x)) 
  
  code = ''.join(random_list) 
  
  return code  

Vote = [{
    "id": 1,
    "code": random_code(),
    "boton1": "",
    "boton2": ""
}]

@app.get("/")
def read_root():
    return {"Welcome": "Welcome to Studention API"}

@app.get("/vote/")
def get_vote():
    return Vote

@app.post("/vote/")
def create_vote(vote: Vote):
    vote.code = random_code()
    return Vote

@app.get("/vote/{vote_id}")
def read_vote(vote_id: int):
    return {"Vote": vote_id, "Code": Vote.code, "Boton1": Vote.boton1, "Boton2": Vote.boton1}

@app.put("/vote/{vote_id}")
def update_item(vote_id: int, vote: Vote):
    return {"Vote_id": vote_id, "Code": Vote.code, "Boton1": Vote.boton1, "Boton2": Vote.boton1}