from flask import Flask, request, jsonify
import subprocess
import uuid
import os

app = Flask(__name__)

@app.route('/executar', methods=['POST'])
def executar_script():
    arquivo = request.files.get('arquivo')
    if not arquivo:
        return jsonify({'erro': 'Nenhum arquivo enviado'}), 401
    
    script_id = str(uuid.uuid4())
    path = f"/tmp/{script_id}.py"
    arquivo.save(path)
    
    try:
        resultado = subprocess.check_output(['python3', path], stderr=subprocess.STDOUT, timeout=10)
        os.remove(path)
        return jsonify({'saida': resultado.decode('utf-8')})
    except subprocess.CalledProcessError as e:
        os.remove(path)
        return jsonify({'erro': e.output.decode('utf-8')}), 400
    except subprocess.TimeoutExpired:
        os.remove(path)
        return jsonify({'erro': 'Execução demorou demais'}), 408

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)

