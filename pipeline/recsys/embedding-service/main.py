import json
from sentence_transformers import SentenceTransformer
from typing import Union
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import torch

app = FastAPI()

origins = ["*"]
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.on_event("startup")
def load_model():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

    model = SentenceTransformer("all-MiniLM-L6-v2")
    model.to(device)

    app.state.model = model
    app.state.device = str(device)

@app.get("/")
def read_root():
    return {
        "description": "This is an API for embedding slug-name",
        "endpoints": {
            "/health": {"method": "GET"},
            "/embedding": {"method": "GET"},
            "/device": {"method": "GET"}
        }
    }

@app.get("/health")
def read_health():
    return {"status": "ok"}

@app.get("/embedding")
def read_embedding(q: Union[str, None] = None):
    model = app.state.model

    sentences = [q]
    sentence_embeddings = model.encode(sentences)

    return {
        "q": q,
        "embedding": sentence_embeddings[0].tolist()
    }

@app.get("/device")
def read_device():
    return {"device": app.state.device}