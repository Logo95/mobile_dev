import sqlite3

# Создаём соединение с базой данных
conn = sqlite3.connect('bank.db')
cursor = conn.cursor()

# Создаём таблицу для хранения кешбэка
cursor.execute('''
CREATE TABLE IF NOT EXISTS cashback (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    month TEXT NOT NULL,
    amount INTEGER NOT NULL
)
''')

# Заполняем таблицу начальными данными
cursor.execute('INSERT INTO cashback (month, amount) VALUES (?, ?)', ('current', 1500))
cursor.execute('INSERT INTO cashback (month, amount) VALUES (?, ?)', ('previous', 1200))

# Сохраняем изменения и закрываем соединение
conn.commit()
conn.close()

print("База данных успешно создана и заполнена данными.")
