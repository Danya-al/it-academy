# IT Academy · Java Backend

В этом репозитории я собираю практику с курса Java Backend. Начал с консольной программы для разбора аргументов, затем сделал Spring Boot API, подключил PostgreSQL, добавил DTO, обработку ошибок и тесты. Упражнения по дженерикам и рефлексии можно запускать отдельно от приложения.

| Каталог | Что внутри |
| --- | --- |
| [`args-app`](args-app) | Консольная программа. Выводит аргументы командной строки, определяет, какие из них являются целыми числами, и считает их сумму. |
| [`text-api`](text-api) | REST API для обработки текста, работы с каталогом библиотеки и обмена сообщениями между экземплярами приложения. Здесь же находятся упражнения по Java. |

**Стек** — Java 17, Spring Boot, Gradle Wrapper, PostgreSQL, Spring Data JPA, Hibernate, Jackson 3, MapStruct, Lombok и JUnit.

## Что реализовано

- **Текст.** API удаляет гласные, считает согласные и строит частотный словарь. Подсчёт символов реализован двумя способами, через цикл и через Stream API.
- **Библиотека.** Сущности книг, авторов, читателей, сотрудников и выдач связаны через JPA. API показывает книги и фильтрует их по числу страниц или фамилии автора. Для авторов доступны просмотр, создание и обновление.
- **Чат.** Один экземпляр приложения отправляет сообщение другому по HTTP через `RestTemplate`. Получатель сохраняет сообщение в PostgreSQL. Историю можно отфильтровать по имени или IP отправителя.
- **DTO и JSON.** `BookMapper` и `AuthorMapper` преобразуют сущности в DTO через MapStruct. Отдельные эндпоинты показывают, как вручную читать и записывать JSON через Jackson. `GlobalExceptionHandler` приводит ошибки разбора JSON, ошибки параметров и ответы `ResponseStatusException` к формату `ErrorResponse`.
- **Дженерики.** `GenericUtils` реализует пузырьковую сортировку массивов и изменяемых списков. `searchInList` возвращает `Tuple<Integer, T>` с индексом и элементом или `(-1, null)`, если элемент отсутствует.
- **Рефлексия.** `ReflectionDemo` читает и изменяет приватное поле `Cat` и вызывает приватный метод. `ReflectionMapper.fill` заполняет поля объекта по ключам `Map<String, ?>`. Он преобразует строки, целые и дробные числа, а также `LocalDateTime`. Например, строка `"5"` становится значением `int` в поле `age`.

## Запуск

### Spring Boot API

Понадобятся JDK 17 и PostgreSQL. По умолчанию приложение подключается к базе `it_academy` на `localhost:5432`. Настройки находятся в [`application.properties`](text-api/src/main/resources/application.properties). Для локального переопределения подключения можно задать `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` и `SPRING_DATASOURCE_PASSWORD`.

В базе должны быть две схемы:

| Схема | Что подготовить |
| --- | --- |
| `library` | Таблицы `book`, `author`, `book_author`, `reader`, `employee` и `loan` из проекта по библиотеке. SQL для этой схемы хранится отдельно от репозитория. |
| `chat` | Схему и таблицу `message` можно создать скриптом [`text-api/db/chat.sql`](text-api/db/chat.sql). |

Hibernate запускается с `ddl-auto=validate`. Он проверяет структуру базы при старте, поэтому таблицы нужно подготовить заранее.

Из корня репозитория в PowerShell:

```powershell
cd text-api
.\gradlew.bat bootRun
```

После запуска API доступно на `http://localhost:8080`. В Linux и macOS из каталога `text-api` используется `./gradlew bootRun`.

### Консольные упражнения

В IntelliJ IDEA их можно запустить через `main`. PostgreSQL для этих упражнений не нужен.

| Класс | Что проверить |
| --- | --- |
| [`args-app → Main`](args-app/src/ru/academy/args/Main.java) | Передай `10 hello -3` в **Program arguments**. Программа выведет все три аргумента и сумму `7`. |
| [`GenericsDemo`](text-api/src/main/java/ru/academy/textapi/practice/generics/GenericsDemo.java) | Сортировка `String[]`, `Double[]` и `List<Integer>`, поиск существующего и отсутствующего элемента. |
| [`ReflectionDemo`](text-api/src/main/java/ru/academy/textapi/practice/reflection/ReflectionDemo.java) | Доступ к приватным членам класса и заполнение `Cat` из карты. Итог маппинга — `Cat(name=Тишка, age=5)`. |

## Справочник по API

| Метод и путь | Что делает |
| --- | --- |
| `GET /api/text/remove-vowels?text=hello` | Возвращает текст без гласных. |
| `GET /api/text/consonants-count?text=hello` | Возвращает число согласных. |
| `GET /api/text/letter-map?text=Hello` | Считает буквы без учёта регистра. |
| `GET /api/text/char-count?text=banana` | Считает все символы. У `/api/text/char-count-stream` тот же результат через Stream API. |
| `GET /api/books` | Возвращает список книг с авторами. |
| `GET /api/books/more-than?pages=100` | Возвращает книги, в которых строго больше 100 страниц. Есть также фиксированный маршрут `/api/books/more-than-100-pages`. |
| `GET /api/books/by-author?lastName=Толстой` | Ищет книги по фамилии автора. В ответе только название и число страниц. |
| `POST /api/books/parse-json` | Читает JSON из тела запроса и преобразует его в `BookDto`. |
| `GET /api/books/as-json-string` | Сериализует список `BookDto` в JSON вручную. |
| `GET /api/authors` · `GET /api/authors/{id}` | Возвращает список авторов или одного автора по ID. |
| `POST /api/authors` · `PUT /api/authors/{id}` | Создаёт или обновляет автора. Тело запроса — JSON с полями `firstName`, `lastName`, `middleName`, `birthYear`, `country`. |
| `POST /api/chat/send-message` | Отправляет сообщение. Тело запроса содержит `ip`, `senderName` и `message`. В `ip` можно передать адрес с портом. |
| `POST /api/chat/receive-message` | Принимает JSON с `senderName` и `message` и сохраняет его. |
| `GET /api/chat/messages` · `GET /api/chat/messages/from?name=Demo` | Возвращает историю сообщений. Маршрут `/from` также принимает фильтр `ip`. |

Быстрая проверка из PowerShell после старта приложения:

```powershell
Invoke-RestMethod 'http://localhost:8080/api/text/char-count?text=banana'
Invoke-RestMethod 'http://localhost:8080/api/books/more-than?pages=100'
```

Для проверки обработки ошибок можно открыть `/api/books/more-than?pages=abc`. API вернёт `400 Bad Request` с полями `status`, `error`, `message` и `timestamp`. Маршруты `/api/exceptions/checked`, `/unchecked` и `/caught` в контроллере исключений демонстрируют разные способы работы с исключениями.

## Тесты

Из каталога `text-api`:

```powershell
# Пять тестов преобразования BookDto в JSON и обратно. База не нужна.
.\gradlew.bat test --tests '*BookDtoJsonTest'

# Весь набор, включая загрузку Spring-контекста. Нужна подготовленная база.
.\gradlew.bat test
```

`BookDtoJsonTest` проверяет сохранение вложенных данных при преобразовании в JSON и обратно, обработку `null` и отсутствующих полей, а также ошибку при некорректном JSON.

## Шпаргалка по коду

| Тема | Что вспомнить | Где смотреть |
| --- | --- | --- |
| Spring MVC и DI | Контроллер принимает HTTP-запрос, сервис выполняет логику, репозиторий обращается к базе. Зависимости передаются через конструктор. | `BookController` → `BookService` → `BookRepository` |
| `JpaRepository<T, ID>` | `T` задаёт сущность, `ID` — тип идентификатора. Spring Data предоставляет `findAll`, `findById` и `save` и строит запросы по именам методов, например `findDistinctByAuthors_LastName`. | `BookRepository`, `AuthorService` |
| JPA-связи | У книги может быть несколько авторов, у автора — несколько книг (`@ManyToMany`). Каждая выдача связана с одной книгой, одним читателем и одним сотрудником (`@ManyToOne`). | `Book`, `Loan` |
| DTO, MapStruct и Jackson | DTO описывает данные API. MapStruct генерирует преобразования между Java-объектами при компиляции. Jackson преобразует Java-объекты в JSON и обратно. ModelMapper разбирался на курсе как пример маппинга во время выполнения. | `BookDto`, `BookMapper`, `BookService` |
| Дженерики | `<T>` позволяет переиспользовать алгоритм с разными типами. Ограничение `Comparable<T>` даёт доступ к `compareTo` при сортировке. `Tuple<A, B>` хранит пару значений разных типов. | `GenericUtils`, `Tuple` |
| Рефлексия | `getDeclaredField` и `getDeclaredMethod` находят члены класса по имени. `Field.get` и `Field.set` читают и записывают поле, `Method.invoke` вызывает метод. | `ReflectionDemo`, `ReflectionMapper` |

[Материалы курса](https://github.com/dmitry-kolesov/it-academy)
