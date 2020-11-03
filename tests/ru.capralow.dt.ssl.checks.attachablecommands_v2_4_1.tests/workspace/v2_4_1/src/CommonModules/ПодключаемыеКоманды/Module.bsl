#Область ПрограммныйИнтерфейс

// Выводит подключенные команды в форме.
//
// Параметры:
//   Форма - УправляемаяФорма - Форма, в которой необходимо разместить команды.
//   ПараметрыРазмещения - Неопределено, Структура - Параметры размещения команд.
//       Используется когда в форме несколько списков
//       (в этом случае размещается несколько вызовов этой процедуры с указанием 2го параметра),
//       или когда типы источников зависят от параметров открытия формы.
//       Состав ключей см. в функции ПараметрыРазмещения().
//
//@skip-warning
Процедура ПриСозданииНаСервере(Форма, ПараметрыРазмещения = Неопределено) Экспорт
	
КонецПроцедуры

// Конструктор одноименного параметра процедуры ПриСозданииНаСервере.
//
// Возвращаемое значение:
//   Структура - Параметры размещения подключаемых команд.
//       * Источники - ОписаниеТипов, Массив из ОбъектМетаданных - Источники команд.
//           Используется для второстепенных списков,
//           а также в формах объектов, не являющихся поставщиками команд (обработки, общие формы).
//       * КоманднаяПанель - ГруппаФормы - Командная панель или группа команд, в которой выводятся подменю.
//           Используется как родитель для создания подменю в случае их отсутствия.
//           Если не указан то в первую очередь ищется группа "ПодключаемыеКоманды".
//       * ПрефиксГрупп - Строка - Добавка к именам подменю и имени командной панели.
//           Используется при необходимости префиксации групп с командами (в частности, когда в форме несколько таблиц).
//           В качестве префикса рекомендуется использовать имя таблицы формы, для которой выводятся команды.
//           Например, если ПрефиксГрупп = "СкладскиеДокументы" (имя второстепенной таблицы формы),
//           то используются подменю с именами "СкладскиеДокументыПодменюПечать", "СкладскиеДокументыПодменюОтчеты" и т.д.
//
//@skip-warning
Функция ПараметрыРазмещения() Экспорт

КонецФункции

// Обработчик команды формы, требующей контекстный вызов сервера.
//
// Параметры:
//   Форма - УправляемаяФорма - Форма, из которой выполняется команда.
//   ПараметрыВызова - Структура - Параметры вызова.
//   Источник - ТаблицаФормы, ДанныеФормыСтруктура - Объект или список формы с полем "Ссылка".
//   Результат - Структура - Результат выполнения команды.
//
//@skip-warning
Процедура ВыполнитьКоманду(Знач Форма, Знач ПараметрыВызова, Знач Источник, Результат) Экспорт

КонецПроцедуры

// Задает условия видимости команды на форме в зависимости от контекста.
//
// Параметры:
//   Команда      - СтрокаТаблицыЗначений - Команда, для которой добавляется условие видимости.
//   Реквизит     - Строка                - Имя реквизита объекта.
//   Значение     - Произвольный          - Значение реквизита объекта.
//   ВидСравнения - ВидСравненияКомпоновкиДанных - Вид сравнения значений.
//       Допустимо использовать следующие виды сравнения:
//         ВидСравненияКомпоновкиДанных.Равно,
//         ВидСравненияКомпоновкиДанных.НеРавно,
//         ВидСравненияКомпоновкиДанных.Заполнено,
//         ВидСравненияКомпоновкиДанных.НеЗаполнено,
//         ВидСравненияКомпоновкиДанных.ВСписке,
//         ВидСравненияКомпоновкиДанных.НеВСписке,
//         ВидСравненияКомпоновкиДанных.Больше,
//         ВидСравненияКомпоновкиДанных.Меньше,
//         ВидСравненияКомпоновкиДанных.БольшеИлиРавно,
//         ВидСравненияКомпоновкиДанных.МеньшеИлиРавно.
//       Значение по умолчанию: ВидСравненияКомпоновкиДанных.Равно.
//
//@skip-warning
Процедура ДобавитьУсловиеВидимостиКоманды(Команда, Реквизит, Значение, Знач ВидСравнения = Неопределено) Экспорт

КонецПроцедуры

#КонецОбласти
