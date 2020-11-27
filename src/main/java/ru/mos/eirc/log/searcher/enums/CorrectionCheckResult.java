package ru.mos.eirc.log.searcher.enums;

import lombok.Getter;

@Getter
public enum CorrectionCheckResult {
    ERROR1(1,  "Нижняя граница превосходит верхнюю"),
    ERROR18(18,  "Некорректное значение нижней границы"),
    ERROR19(19,  "Недопустимый формат временного интервала"),
    ERROR37(37,  "Не все обязательные поля заполнены"),
    ERROR44(44,  "Неверно указано расположение логов"),
    ERROR3701(3701, "Не указано расширение файла для асинхронного метода");

    private final Long errorCode;
    private final String errorMessage;

    CorrectionCheckResult(long errorCode, String errorMessage) {
	this.errorCode = errorCode;
	this.errorMessage = errorMessage;
    }
}