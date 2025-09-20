import zipfile
import subprocess
import tempfile
import os
import glob
from flask import Flask, request, jsonify
import logging
import requests
app = Flask(__name__)

@app.route('/executar', methods=['POST'])
def executar():
    code = request.files['arquivo']
    lang = request.form['lang'].strip()
    logging.basicConfig(level=logging.INFO)

    with tempfile.TemporaryDirectory() as tmpdir:
        if lang == 'R':

            file_path = os.path.join(tmpdir, 'code.R')
            code.save(file_path)
            with open(file_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.121:8000/execute",  # endpoint do executor
                    files={"code": f},
                    data={"lang": "R"}  # boa prática: nome e MIME
                ) 

        elif lang == 'Java':

            zip_path = os.path.join(tmpdir, 'codigo.zip')
            code.save(zip_path)

            with open(zip_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.121:8000/execute",  # endpoint do executor
                    files={"code": f},
                    data={"lang": "Java"}  # boa prática: nome e MIME
                )  
                    
        elif lang == 'C SMPL':
            file_path = os.path.join(tmpdir, 'code.c')
            with open(file_path, 'w') as f:
                code.save(file_path)
            
            with open(file_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.121:8000/execute",  # endpoint do executor
                    files={"code": f},
                    data={"lang": "C SMPL"}  # boa prática: nome e MIME
                )
        elif lang == 'C SMPLX':
            file_path = os.path.join(tmpdir, 'code.c')
            with open(file_path, 'w') as f:
                code.save(file_path)
            
            with open(file_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.121:8000/execute",  # endpoint do executor
                    files={"code": f},
                    data={"lang": "C SMPLX"}  # boa prática: nome e MIME
                )
        
        else:
            file_path = os.path.join(tmpdir, 'code.py')
            code.save(file_path)
            
            with open(file_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.121:8000/execute",  # endpoint do executor
                    files={"code": ("code.py", f, "text/x-python")},
                    data={"lang": "Python"}  # boa prática: nome e MIME
                )

        return resp.json()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)


