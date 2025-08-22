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
        if lang == 'Python':
            file_path = os.path.join(tmpdir, 'code.py')
            code.save(file_path)
            
            with open(file_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.252:8000/execute",  # endpoint do executor
                    files={"code": ("code.py", f, "text/x-python")},
                    data={"lang": "Python"}  # boa prática: nome e MIME
                )

        elif lang == 'Java':
            jar_path = os.path.join(os.path.dirname(__file__), 'javasim-2.3.jar')
            if not os.path.exists(jar_path):
                raise FileNotFoundError(f"Arquivo .jar não encontrado: {jar_path}")
            else:
                logging.info(f"JAR localizado: {jar_path}")

            zip_path = os.path.join(tmpdir, 'codigo.zip')
            code.save(zip_path)
            with zipfile.ZipFile(zip_path, 'r') as zip_ref:
                zip_ref.extractall(tmpdir)
            java_files = glob.glob(os.path.join(tmpdir, "*.java"))    
            if not java_files:
                logging.error("Nenhum arquivo .java encontrado!")
            else:
                logging.info("Compilando arquivos: %s", java_files)

                compile_cmd = ['javac', '-cp', jar_path] + java_files
                compile_proc = subprocess.run(compile_cmd, capture_output=True, text=True)

                if compile_proc.returncode != 0:
                    logging.error("Erro na compilação:")
                    logging.error("STDERR:\n%s", compile_proc.stderr.strip())
                    logging.error("STDOUT:\n%s", compile_proc.stdout.strip())
                else:
                    logging.info("Compilação bem-sucedida!")

        # Executar a Main
                    run_cmd = ['java', '-cp', f'{tmpdir}:{jar_path}', 'Main']
                    proc = subprocess.run(run_cmd, capture_output=True, text=True)

                    logging.info("Saída da execução:")
                    logging.info("STDOUT:\n%s", proc.stdout.strip())
                    logging.info("STDERR:\n%s", proc.stderr.strip())
                    
        elif lang == 'C SMPL':
            file_path = os.path.join(tmpdir, 'code.c')
            with open(file_path, 'w') as f:
                f.write(code.read().decode())

            # Ajuste para onde está instalada sua biblioteca SMPL
            smpl_include_path = '/usr/local/include'  # Ou onde estiver o smpl.h
            smpl_lib_path = '/usr/local/lib'          # Ou onde estiver libsmpl.a/.so

            output_binary = os.path.join(tmpdir, 'code_exec')

            compile_cmd = [
                'gcc',
                file_path,
                '-I', smpl_include_path,
                '-L', smpl_lib_path,
                '-lsmpl',
                '-o', output_binary
            ]

            compile_proc = subprocess.run(compile_cmd, capture_output=True, text=True)

            if compile_proc.returncode != 0:
                logging.error("Erro na compilação C SMPL:")
                logging.error("STDERR:\n%s", compile_proc.stderr.strip())
                logging.error("STDOUT:\n%s", compile_proc.stdout.strip())
                return jsonify({
                    'stdout': compile_proc.stdout,
                    'stderr': compile_proc.stderr,
                    'returncode': compile_proc.returncode
                })

            # Executar o binário
            proc = subprocess.run([output_binary], capture_output=True, text=True, timeout=10)
        
        else:
            file_path = os.path.join(tmpdir, 'code.R')
            code.save(file_path)
            with open(file_path, "rb") as f:
                resp = requests.post(
                    "http://192.168.100.252:8000/execute",  # endpoint do executor
                    files={"code": f},
                    data={"lang": "R"}  # boa prática: nome e MIME
                )

        #return jsonify({
        #    'stdout': proc.stdout,
        #    'stderr': proc.stderr,
        #    'returncode': proc.returncode
        #})
        return resp.json()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)


