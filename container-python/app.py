import zipfile
import subprocess
import tempfile
import os
from flask import Flask, request, jsonify
import logging
app = Flask(__name__)

@app.route('/executar', methods=['POST'])
def executar():
    code = request.files['arquivo']
    lang = request.form['lang'].strip()
    logging.basicConfig(level=logging.INFO)

    with tempfile.TemporaryDirectory() as tmpdir:
        if lang == 'Python':
            file_path = os.path.join(tmpdir, 'code.py')
            with open(file_path, 'w') as f:
                f.write(code.read().decode())
            cmd = ['python3', file_path]

        elif lang == 'Java':
            javasim_jar = os.path.abspath('javasim-2.3.jar')
            zip_path = os.path.join(tmpdir, 'codigo.zip')
            code.save(zip_path)
            with zipfile.ZipFile(zip_path, 'r') as zip_ref:
                zip_ref.extractall(tmpdir)
            compile_proc = subprocess.run(
                ['javac', '-cp', javasim_jar, '*.java'],
                cwd=tmpdir,
                capture_output=True,
                text=True,
                shell=True  # necessário por causa do *.java
            )
            if compile_proc.returncode != 0:
                return jsonify({'error': compile_proc.stderr}), 400
            cmd = [
                'java',
                '-cp',
                f'.:{javasim_jar}',
                'Main'
            ]    
            try:
                proc = subprocess.run(
                    cmd,
                    cwd=tmpdir,
                    capture_output=True,
                    text=True,
                    timeout=10
                )   
            except subprocess.TimeoutExpired:
                return jsonify({'error': 'Execução excedeu o tempo limite'}), 408
        
        else:
            file_path = os.path.join(tmpdir, 'code.R')
            with open(file_path, 'w') as f:
                f.write(code.read().decode())
            cmd = ['Rscript', file_path]

        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=10)

        return jsonify({
            'stdout': proc.stdout,
            'stderr': proc.stderr,
            'returncode': proc.returncode
        })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)


