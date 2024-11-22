import google.generativeai as genai

class AI_call:
    def __init__(self):
        genai.configure(api_key="AIzaSyBcbKOXuePtMGvotNgYHwl6Fnlk-U2eJJU")
        model = genai.GenerativeModel("gemini-1.5-flash")
        response = model.generate_content("""Compare the dance with these frame and give feedback in terms of 3D motion, so that frame set 1 can match frame set 2
    nose: 
    Frame 0: x=0.5138, y=0.3390, z=-0.1684
  Frame 1: x=0.5138, y=0.3388, z=-0.1561
  Frame 2: x=0.5136, y=0.3386, z=-0.1257
  Frame 3: x=0.5134, y=0.3378, z=-0.1126
  Frame 4: x=0.5133, y=0.3377, z=-0.1169
  Frame 5: x=0.4937, y=0.3382, z=-0.1278
  
  with 
  
  nose:
  Frame 0: x=0.5311, y=0.1840, z=-0.2189
  Frame 1: x=0.5320, y=0.1840, z=-0.2172
  Frame 2: x=0.5335, y=0.1838, z=-0.2186
  Frame 3: x=0.5347, y=0.1839, z=-0.2229
  Frame 4: x=0.5354, y=0.1844, z=-0.2256
  """)
        print(response.text)

if __name__ == "__main__":
    AI_call()

