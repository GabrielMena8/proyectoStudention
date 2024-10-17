from typing import Union

from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class Vote(BaseModel):
    code: int
    value: bool
    is_offer: Union[bool, None] = None


@app.get("/")
def read_root():
    return {"StudentionAPI": Vote}


@app.get("/votes/{id}")
def read_item(vote_id: int, q: Union[str, None] = None):
    return {"vote_id": vote_id, "q": q}

@app.put("/votes/{id}")
def update_item(vote_id: int, vote: Vote):
    return {"vote_code": vote.code, "vote_id": vote_id}