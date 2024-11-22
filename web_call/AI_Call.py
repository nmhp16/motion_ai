import google.generativeai as genai
import sys

class AI_call:
    def __init__(self, prompt):
        genai.configure(api_key="AIzaSyBcbKOXuePtMGvotNgYHwl6Fnlk-U2eJJU")
        model = genai.GenerativeModel("gemini-1.5-flash")
        response = model.generate_content(prompt)
        print(response.text, flush=True) # Print result to output

if __name__ == "__main__":
    # Read the prompt passed from Java
    prompt = sys.argv[1]
    AI_call(prompt)

