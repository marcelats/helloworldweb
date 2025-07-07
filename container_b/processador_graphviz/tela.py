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
    saida_path = f"/tmp/{uuid.uuid4().hex}.py"

    uploaded.save(entrada_path)
    logging.basicConfig(level=logging.INFO)
    logging.info(f"lang recebido: {repr(request.form['lang'])}")
    # Aqui voc chama seu gerador com entrada_path como entrada e saida_path como saída
    # Exemplo fictício
    if lang == 'Python':
        py = geradorPython.GeradorPython(entrada_path)
        py.principal()
        name = py.nome()
        codigo_gerado = py.nome()
        os.rename(f"codigo/{codigo_gerado}", saida_path)

        return send_file(saida_path, as_attachment=True, download_name="codigo.py")

    elif lang == 'Java':
        java = geradorJava.GeradorJava(entrada_path)
        java.principal()
        name = java.nome()
        codigo_gerado = java.nome()
        os.rename(f"codigo/{codigo_gerado}", saida_path)

        return send_file(saida_path, as_attachment=True, download_name="codigo.java")

    else:
        r = geradorR.GeradorR(entrada_path)
        r.principal()
        name = r.nome()
        codigo_gerado = r.nome()
        os.rename(f"codigo/{codigo_gerado}", saida_path)

        return send_file(saida_path, as_attachment=True, download_name="codigo.r")


app.run(host="0.0.0.0", port=8000)

