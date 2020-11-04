///////////////////////////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2019, ООО 1С-Софт
// Все права защищены. Эта программа и сопроводительные материалы предоставляются 
// в соответствии с условиями лицензии Attribution 4.0 International (CC BY 4.0)
// Текст лицензии доступен по ссылке:
// https://creativecommons.org/licenses/by/4.0/legalcode
///////////////////////////////////////////////////////////////////////////////////////////////////////

#Область ПрограммныйИнтерфейс

// Выводит подключенные команды в форме.
//
// Параметры:
//   Форма - ФормаКлиентскогоПриложения - форма, в которой необходимо разместить команды.
//   ПараметрыРазмещения - Структура - параметры размещения команд.
//       Используется, когда в форме несколько списков
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
//   Структура - параметры размещения подключаемых команд.
//       * Источники - ОписаниеТипов, Массив - источники команд.
//           Используется для второстепенных списков, а также в формах объектов, не являющихся поставщиками
//           команд (обработки, общие формы). В массиве ожидаются элементы типа "ОбъектМетаданных".
//       * КоманднаяПанель - ГруппаФормы - командная панель или группа команд, в которой выводятся подменю.
//           Используется как родитель для создания подменю в случае их отсутствия.
//           Если не указан то в первую очередь ищется группа "ПодключаемыеКоманды".
//       * ПрефиксГрупп - Строка - добавка к именам подменю и имени командной панели.
//           Используется при необходимости префиксации групп с командами (в частности, когда в форме несколько таблиц).
//           В качестве префикса рекомендуется использовать имя таблицы формы, для которой выводятся команды.
//           Например, если ПрефиксГрупп = "СкладскиеДокументы" (имя второстепенной таблицы формы),
//           то используются подменю с именами "СкладскиеДокументыПодменюПечать", "СкладскиеДокументыПодменюОтчеты" и т.д.
//
//@skip-warning
Функция ПараметрыРазмещения() Экспорт

КонецФункции

// Обработчик команды формы, требующей контекстного вызова сервера.
//
// Параметры:
//   Форма - ФормаКлиентскогоПриложения - форма, из которой выполняется команда.
//   ПараметрыВызова - Структура - параметры вызова.
//   Источник - ТаблицаФормы, ДанныеФормыСтруктура - объект или список формы с полем "Ссылка".
//   Результат - Структура - результат выполнения команды.
//
//@skip-warning
Процедура ВыполнитьКоманду(Знач Форма, Знач ПараметрыВызова, Знач Источник, Результат) Экспорт

КонецПроцедуры

// Задает условия видимости команды на форме в зависимости от контекста.
//
// Параметры:
//   Команда      - СтрокаТаблицыЗначений - команда, для которой добавляется условие видимости.
//   Реквизит     - Строка                - имя реквизита объекта.
//   Значение     - Произвольный          - значение реквизита объекта. Параметр обязательный для всех видов
//                                          сравнения кроме Заполнено и НеЗаполнено.
//   ВидСравнения - ВидСравненияКомпоновкиДанных - вид сравнения значений.
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
//       Значение по умолчанию - ВидСравненияКомпоновкиДанных.Равно.
//
//@skip-warning
Процедура ДобавитьУсловиеВидимостиКоманды(Команда, Реквизит, Значение, Знач ВидСравнения = Неопределено) Экспорт

КонецПроцедуры

#КонецОбласти
