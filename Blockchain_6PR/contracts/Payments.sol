// SPDX-License-Identifier: UNLICENSED
// Эта строка указывает лицензию, в данном случае, что код не имеет лицензии и не защищен авторскими правами.

pragma solidity ^0.8.0;
// Эта строка указывает версию языка Solidity, которую следует использовать.

contract Payments {
    // Объявление контракта "Payments".

    struct Payment {
        // Объявление структуры "Payment", которая представляет информацию о платеже.
        uint amount; // Сумма платежа в wei.
        uint timestamp; // Метка времени платежа.
        address from; // Адрес отправителя платежа.
        string message; // Сообщение, связанное с платежом.
    }

    struct Balance {
        // Объявление структуры "Balance", которая содержит информацию о балансе пользователя.
        uint totalPayments; // Общее количество платежей пользователя.
        mapping(uint => Payment) payments; // Маппинг для хранения платежей пользователя.
    }

    mapping(address => Balance) public balances;
    // Маппинг для связывания адресов с балансами пользователей.

    function currentBalance() public view returns(uint) {
        // Функция для получения текущего баланса контракта.
        return address(this).balance;
    }

    function getPayment(address _addr, uint _index) public view returns(Payment memory) {
        // Функция для получения информации о конкретном платеже пользователя по индексу.
        return balances[_addr].payments[_index];
    }

    function pay(string memory message) public payable returns(uint) {
        // Функция для выполнения платежа от отправителя контракта.

        uint paymentNum = balances[msg.sender].totalPayments;
        // Получение текущего номера платежа для отправителя.
        balances[msg.sender].totalPayments++;
        // Увеличение счетчика общего количества платежей отправителя.

        Payment memory newPayment = Payment(
            msg.value, // Сумма платежа в wei.
            block.timestamp, // Метка времени платежа.
            msg.sender, // Адрес отправителя.
            message // Сообщение, связанное с платежом.
        );

        balances[msg.sender].payments[paymentNum] = newPayment;
        // Сохранение информации о платеже в баланс отправителя.

        return msg.value; // Возвращение суммы платежа.
    }
}
