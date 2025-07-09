import zipfile
import subprocess
import tempfile
import os
import glob
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
            proc = subprocess.run(cmd, capture_output=True, text=True, timeout=10)

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
          #  compile_proc = subprocess.run(
               # ['javac', '-cp', javasim_jar, '*.java'],
               # cwd=tmpdir,
              #  capture_output=True,
             #   text=True,
            #    shell=True  # necessário por causa do *.java
           # )
           # if compile_proc.returncode != 0:
              #  logging.error("STDERR:\n%s", compile_proc.stderr)
             #   logging.error("STDOUT:\n%s", compile_proc.stdout)
            #    return jsonify({'error': compile_proc.stderr}), 400
           # cmd = [
              #  'java',
             #   '-cp',
            #    f'.:{javasim_jar}',
           #     'Main'
          #  ]    
         #   try:
        #        proc = subprocess.run(
       #             cmd,
      #              cwd=tmpdir,
     #               capture_output=True,
    #                text=True,
   #                 timeout=10
  #              )   
 #           except subprocess.TimeoutExpired:
#                return jsonify({'error': 'Execução excedeu o tempo limite'}), 408
        
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


