from random import randint
from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

vote = []
class Vote(BaseModel):
    code: str = None
    boton1: bool
    boton2: bool

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
def get_code():
    return Vote, Vote.code

@app.post("/vote/")
def create_vote(vote: Vote):
    vote.code = random_code()
    return Vote

@app.post("/vote/1")
def create_vote(codeESP, vote: Vote):
    vote.code = codeESP
    vote.boton1 = True
    vote.boton2 = False
    return Vote

@app.post("/vote/2")
def create_vote(codeESP, vote: Vote):
    vote.code = codeESP
    vote.boton1 = True
    vote.boton2 = False
    return Vote

@app.get("/vote/{id}")
def read_vote(id: int):
    return {"Vote": id, "Code": Vote.code, "Boton1": Vote.boton1, "Boton2": Vote.boton1}

@app.put("/vote/{id}")
def update_item(id: int, vote: Vote):
    return {"Vote_id": vote_id, "Code": Vote.code, "Boton1": Vote.boton1, "Boton2": Vote.boton1}