package ru.bmstu.log.searcher.service;

import ru.bmstu.logsearch.SearchInfo;
import ru.bmstu.logsearch.SearchInfoResult;

import javax.jws.WebService;

@WebService
public interface LogSearchService {

    /**
     * Поиск логов
     *
     * @param searchInfo входные данные
     * @return результат операции
     */
    SearchInfoResult logSearch(SearchInfo searchInfo);

    /**
     * Генерация файлов по найденным логам
     *
     * @param searchInfo       входные данные
     * @param searchInfoResult найденные логи
     */
    void fileGenerate(SearchInfo searchInfo, SearchInfoResult searchInfoResult);

}
