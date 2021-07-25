// Страницы
// а) Поиск логов
// б) Результат синхронного запроса
// в) Страница аутентификации
// Компоненты
// 1) Регулярное выражение
Vue.component('RegularExpression', {
    template:
        '<div>' +
        '<h3>Введите регулярное выражение</h3>' +
        '<input placeholder="Регулярное выражение">' +
        '</div>'
})
// 2) Интервалы (даты) с добавлением по кнопке
Vue.component('DateIntervals', {
    template:
        '<div>' +
        '<h3>Выберите временные промежутки</h3>' +
        '<table>' +
        '<thead>' +
        '<tr>' +
            '<th>#</th>' +
            '<th>from</th>' +
            '<th>to</th>' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '<td></td>' +
        '<td></td>' +
        '<td></td>' +
        '</tbody>' +
        '</table>' +
        '</div>'
})
// 3) Расположение логов на сервере
Vue.component('Location', {
    template:
        '<div>' +
        '<h3>Введите расположение логов</h3>' +
        '<input placeholder="Расположение логов">' +
        '</div>'
})
// 4) Переключатель синхронный\асинхронный запрос
Vue.component('Realization', {
    template:
        '<div>' +
        '<h3>Сохранить результат в файл?</h3>' +
        '<input type="radio" id="no" value="Нет">' +
        '<label for="no">Нет</label>' +
        '<input type="radio" id="yes" value="Да">' +
        '<label for="yes">Да</label>' +
        '</div>'

})
// 5) Выпадающий список расширения файлов
Vue.component('FileExtension', {
    template:
        '<div>' +
        '<h3>Выберите расширение запрашиваемого файла</h3>' +
        '<select>' +
        '  <option disabled value="">Выберите один из вариантов</option>' +
        '  <option>PDF</option>' +
        '  <option>RTF</option>' +
        '  <option>HTML</option>' +
        '  <option>XML</option>' +
        '  <option>LOG</option>' +
        '  <option>DOC</option>' +
        '</select>' +
        '</div>'
})
// 6) Кнопка отправки запроса
Vue.component('Button', {
    template:
        '<div>' +
            '<button style="margin: 15px" type="submit" class="btn btn-lg btn-success">Поиск</button>' +
        '</div>'
})

Vue.component('Footer', {
    template:
        '<div>' +
        '<h1>Footer</h1>' +
        '</div>'
});
Vue.component('Header', {
    template:
        '<div>' +
        '<h1>Header</h1>' +
        '</div>'
});
Vue.component('App', {
    template:
        '<div>' +
        '<Header/>' +
        '<RegularExpression/>' +
        '<DateIntervals/>' +
        '<Location/>' +
        '<Realization />' +
        '<FileExtension />' +
        '<Button />' +
        '<Footer/>' +
        '</div>'
});
new Vue({
    el: '#app',
    template: '<App />'
});
