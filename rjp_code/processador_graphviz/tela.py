import geradorJava, geradorPython, geradorR
import shutil
import gabaritos
import classes
from flask import Flask, request, send_file
import uuid
import os
import logging
app = Flask(__name__)

@app.route('/process', methods=['POST'])
def process():
    if 'file' not in request.files:
        return "File not sent", 401
    if 'lang' not in request.form:
        return "Lang not sent", 401

    uploaded = request.files['file']
    lang = request.form['lang'].strip()
    input_path = f"/tmp/{uuid.uuid4().hex}.gv"
    output_path_py = f"/tmp/{uuid.uuid4().hex}.py"
    output_path_java = os.path.join('/tmp', uuid.uuid4().hex)
    output_path_r = f"/tmp/{uuid.uuid4().hex}.r"

    uploaded.save(input_path)
    logging.basicConfig(level=logging.INFO)
    logging.info(f"lang received: {repr(request.form['lang'])}")

    if lang == 'R':
        r = geradorR.GeradorR(input_path)
        r.principal()
        generated_code = r.nome()
        os.rename(f"codigo/{generated_code}", output_path_r)

        return send_file(output_path_r, as_attachment=True, download_name="code.r")
        
    elif lang == 'Java':
        java = geradorJava.GeradorJava(entrada_path)
        java.principal()
        generated_code = java.nome()
        file = os.path.join('code', generated_code)
        shutil.make_archive(output_path_java, 'zip', file)
        os.rename(f"codigo/{generated_code}", output_path_java)
        return send_file(output_path_java+'.zip', as_attachment=True, download_name="code.zip")

    else:
        py = geradorPython.GeradorPython(input_path)
        py.principal()
        generated_code = py.nome()
        os.rename(f"codigo/{generated_code}", output_path_py)

        return send_file(output_path_py, as_attachment=True, download_name="code.py")


app.run(host="0.0.0.0", port=8000)

