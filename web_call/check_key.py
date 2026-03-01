
import os
from dotenv import load_dotenv

load_dotenv(".env")
load_dotenv(".env.local")

key = os.getenv("GROQ_API_KEY")
print(f"Groq Key found: {'Yes' if key else 'No'}")
if key:
    print(f"Key length: {len(key)}")
    print(f"Key preview: {key[:5]}...")
else:
    # Also check what keys ARE available to help debugging
    print("Available keys in environment:", [k for k in os.environ.keys() if "API" in k])
