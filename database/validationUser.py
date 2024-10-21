from pydantic import BaseModel
from google.cloud import firestore

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