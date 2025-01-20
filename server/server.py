import csv
import datetime
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

# Изменяем формат даты для парсинга 'DD.MM.YYYY'
def read_transactions(file_path):
    transactions = []
    try:
        with open(file_path, mode='r', encoding='utf-8') as file:
            reader = csv.DictReader(file, delimiter=';')
            for row in reader:
                transactions.append({
                    "date": datetime.datetime.strptime(row["date"], "%d.%m.%Y"),  # Здесь изменён формат
                    "amount": float(row["amount"])
                })
    except FileNotFoundError:
        print(f"Файл {file_path} не найден!")
    return transactions


# Вычисляем кэшбэк
def calculate_cashback(transactions, month, year):
    total = sum(
        transaction["amount"]
        for transaction in transactions
        if transaction["date"].month == month and transaction["date"].year == year
    )
    return total * 0.01  # 1% от общей суммы

@app.route('/cashback', methods=['GET'])
def get_cashback():
    # Получаем тип месяца из параметра (current или previous)
    month_type = request.args.get("month")
    
    # Загружаем данные из CSV
    file_path = "transactions.csv"
    transactions = read_transactions(file_path)
    
    # Получаем текущую дату
    today = datetime.datetime.now()
    
    # Логика для текущего или предыдущего месяца
    if month_type == "current":
        month = today.month
        year = today.year
    elif month_type == "previous":
        month = (today - datetime.timedelta(days=30)).month
        year = (today - datetime.timedelta(days=30)).year
    else:
        return jsonify({"error": "Неверный параметр month"}), 400

    # Рассчитываем кэшбэк
    cashback = calculate_cashback(transactions, month, year)
    return jsonify({"cashback": round(cashback, 2)})  # Округляем до 2 знаков

if __name__ == '__main__':
    app.run(debug=True)
