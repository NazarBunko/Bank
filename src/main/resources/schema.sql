-- Таблиця Клієнт
CREATE TABLE Client (
                        ID INT AUTO_INCREMENT PRIMARY KEY,
                        FullName VARCHAR(255) NOT NULL,
                        BirthDate DATE NOT NULL,
                        PassportNumber VARCHAR(20) UNIQUE NOT NULL,
                        Address TEXT,
                        Phone VARCHAR(15),
                        ClientType VARCHAR(50)  -- Наприклад: "Фізична особа", "Юридична особа"
);

-- Таблиця Банківський рахунок
CREATE TABLE Bank_Account (
                              ID INT AUTO_INCREMENT PRIMARY KEY,
                              ClientID INT NOT NULL,
                              Login VARCHAR(255) UNIQUE NOT NULL,
                              Password VARCHAR(200) NOT NULL,
                              FOREIGN KEY (ClientID) REFERENCES Client(ID) ON DELETE CASCADE
);

-- Таблиця Банківська картка
CREATE TABLE Bank_Card (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           BankAccountID INT NOT NULL,
                           CardNumber CHAR(16) UNIQUE NOT NULL,  -- Номер картки (16 цифр)
                           EndDate DATE NOT NULL,
                           CVV INT NOT NULL,
                           CardType VARCHAR(50) NOT NULL,         -- Тип картки: "Універсальна", "Кредитна картка"
                           Balance DECIMAL(15,2) DEFAULT 0.00,    -- Баланс картки
                           FOREIGN KEY (BankAccountID) REFERENCES Bank_Account(ID) ON DELETE CASCADE
);

-- Таблиця Транзакція
CREATE TABLE Transaction (
                             ID INT AUTO_INCREMENT PRIMARY KEY,
                             SenderAccountID INT NOT NULL,
                             ReceiverAccountID INT NOT NULL,
                             Amount DECIMAL(15,2) NOT NULL,
                             TransactionDate DATE NOT NULL,
                             TransactionType VARCHAR(50),  -- Наприклад: "Переказ", "Оплата", "Зняття готівки"
                             FOREIGN KEY (SenderAccountID) REFERENCES Bank_Account(ID) ON DELETE CASCADE,
                             FOREIGN KEY (ReceiverAccountID) REFERENCES Bank_Account(ID) ON DELETE CASCADE
);

-- Таблиця Кредит
CREATE TABLE Credit (
                        ID INT AUTO_INCREMENT PRIMARY KEY,
                        ClientID INT NOT NULL,
                        Amount DECIMAL(15,2) NOT NULL,
                        InterestRate DECIMAL(5,2) NOT NULL,  -- Наприклад: 12.50 (%)
                        TermMonths INT NOT NULL,
                        Status VARCHAR(50),  -- Наприклад: "Активний", "Закритий", "Прострочений"
                        MonthlyPayment DECIMAL(15,2),
                        FOREIGN KEY (ClientID) REFERENCES Client(ID) ON DELETE CASCADE
);
