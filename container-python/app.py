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
    logging.info(f"lang recebido: {repr(request.form['lang'])}")

    if not lang:
        return jsonify({'error': 'lang required'}),400

    if not code:
        return jsonify({'error': 'code required'}), 401

    with tempfile.TemporaryDirectory() as tmpdir:
        if lang == 'Python':
            file_path = os.path.join(tmpdir, 'code.py')
            with open(file_path, 'w') as f:
                f.write(code.read().decode())
            cmd = ['python3', file_path]

        elif lang == 'Java':
            file_path = os.path.join(tmpdir, 'code.java')
            with open(file_path, 'w') as f:
                f.write(code.read().decode())
            # Compilar Java
            compile_proc = subprocess.run(['javac', file_path], capture_output=True, text=True)
            if compile_proc.returncode != 0:
                return jsonify({'error': compile_proc.stderr}), 400
            cmd = ['java', '-cp', tmpdir, 'Main']
        
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


