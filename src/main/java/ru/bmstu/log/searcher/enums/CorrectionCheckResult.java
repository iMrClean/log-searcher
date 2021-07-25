package ru.bmstu.log.searcher.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.bmstu.logsearch.SearchInfoResult;

@Getter
@AllArgsConstructor
public enum CorrectionCheckResult {
    ERROR1(1,  "Нижняя граница превосходит верхнюю"),
    ERROR18(18,  "Некорректное значение нижней границы"),
    ERROR19(19,  "Недопустимый формат временного интервала"),
    ERROR37(37,  "Не все обязательные поля заполнены"),
    ERROR44(44,  "Неверно указано расположение логов"),
    ERROR3701(3701, "Не указано расширение файла для асинхронного метода");

    private final long errorCode;
    private final String errorMessage;

    /**
     * Возврат ошибки пользователю
     *
     * @param correctionCheck enum ошибки
     * @return результат операции
     */
    public static SearchInfoResult getSearchInfoError(CorrectionCheckResult correctionCheck) {
        SearchInfoResult searchInfoResult = new SearchInfoResult();
        searchInfoResult.setErrorCode(correctionCheck.getErrorCode());
        searchInfoResult.setErrorMessage(correctionCheck.getErrorMessage());
        return searchInfoResult;
    }

    /**
     * Возврат ошибки пользователю
     *
     * @param errorCode код ошибки
     * @param errorMessage сообщение об ошибке
     * @return результат операции
     */
    public static SearchInfoResult getSearchInfoError(Long errorCode, String errorMessage) {
        SearchInfoResult searchInfoResult = new SearchInfoResult();
        searchInfoResult.setErrorCode(errorCode);
        searchInfoResult.setErrorMessage(errorMessage);
        return searchInfoResult;
    }

}
