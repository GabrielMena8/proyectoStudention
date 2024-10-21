from random import randint
from typing import Union
from google.cloud import firestore
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

@app.get("/")
def read_root():
    return {"Welcome": "Welcome to Studention API"}

@app.get("/vote/")
def get_vote():
    return Vote

@app.post("/vote/")
def create_vote(vote: Vote):
    vote.code = random_code()
    return vote

@app.get("/vote/{vote_id}")
def read_vote(vote_id: int):
    return {"Vote": vote_id, "Code": Vote.code, "Vote": Vote.vote}

@app.put("/vote/{vote_id}")
def update_item(vote_id: int, vote: Vote):
    return {"Vote_id": vote_id, "Code": Vote.code}

db = firestore.Client()

# Definir modelo Pydantic
class User(BaseModel):
    name: str
    racha: str
    correo: str
    code: str


# Método para obtener datos de Firestore y guardarlos en la API
@app.get("/users")
def get_users():
    docs = db.collection("users").stream()
    users = []
    for doc in docs:
        user_data = doc.to_dict()
        users.append(User(name=user_data["name"], racha=user_data["racha"], correo=user_data["correo"], code=user_data["code"]))
    return users