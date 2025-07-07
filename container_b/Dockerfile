FROM python:3.8-slim-buster


WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt
RUN pip install flask
RUN apt-get update && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
COPY /processador_graphviz/tela.py /app/tela.py
COPY /processador_graphviz/geradorR.py /app/geradorR.py
COPY /processador_graphviz/geradorPython.py /app/geradorPython.py
COPY /processador_graphviz/geradorJava.py /app/geradorJava.py
COPY /processador_graphviz/gabaritos /app/gabaritos
COPY /processador_graphviz/classes /app/classes
COPY /processador_graphviz/codigo /app/codigo

CMD [ "python3", "tela.py" ]
