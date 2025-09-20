import geradorJava, geradorPython, geradorR
import shutil
import gabaritos
import classes
from flask import Flask, request, send_file
import uuid
import os
import logging
app = Flask(__name__)

@app.route('/processar', methods=['POST'])
def processar():
    if 'arquivo' not in request.files:
        return "Arquivo não enviado", 401
    if 'lang' not in request.form:
        return "Parâmetro 'lang' não enviado", 401

    uploaded = request.files['arquivo']
    lang = request.form['lang'].strip()
    entrada_path = f"/tmp/{uuid.uuid4().hex}.gv"
    saida_path_py = f"/tmp/{uuid.uuid4().hex}.py"
    saida_path_java = os.path.join('/tmp', uuid.uuid4().hex)
    saida_path_r = f"/tmp/{uuid.uuid4().hex}.r"

    uploaded.save(entrada_path)
    logging.basicConfig(level=logging.INFO)
    logging.info(f"lang recebido: {repr(request.form['lang'])}")
    # Aqui voc chama seu gerador com entrada_path como entrada e saida_path como saída
    # Exemplo fictício
    if lang == 'R':
        r = geradorR.GeradorR(entrada_path)
        r.principal()
        codigo_gerado = r.nome()
        os.rename(f"codigo/{codigo_gerado}", saida_path_r)

        return send_file(saida_path_r, as_attachment=True, download_name="codigo.r")
        
    elif lang == 'Java':
        java = geradorJava.GeradorJava(entrada_path)
        java.principal()
        codigo_gerado = java.nome()
        arquivo = os.path.join('codigo', codigo_gerado)
        shutil.make_archive(saida_path_java, 'zip', arquivo)
        os.rename(f"codigo/{codigo_gerado}", saida_path_java)
        return send_file(saida_path_java+'.zip', as_attachment=True, download_name="codigo.zip")

    else:
        py = geradorPython.GeradorPython(entrada_path)
        py.principal()
        codigo_gerado = py.nome()
        os.rename(f"codigo/{codigo_gerado}", saida_path_py)

        return send_file(saida_path_py, as_attachment=True, download_name="codigo.py")


app.run(host="0.0.0.0", port=8000)

