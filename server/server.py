from flask import Flask, request, jsonify
import sqlite3

app = Flask(__name__)

# Разрешаем запросы из приложения
from flask_cors import CORS
CORS(app)

# Путь для получения кешбэка
@app.route('/cashback', methods=['GET'])
def get_cashback():
    # Получаем параметр месяца из запроса
    month = request.args.get('month')
    
    # Подключаемся к базе данных
    conn = sqlite3.connect('bank.db')
    cursor = conn.cursor()
    
    # Выполняем запрос
    query = "SELECT amount FROM cashback WHERE month = ?"
    result = cursor.execute(query, (month,)).fetchone()
    
    conn.close()
    
    # Если данные найдены, возвращаем их
    if result:
        return jsonify(result[0])
    else:
        return jsonify({"error": "Данные не найдены"}), 404

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
