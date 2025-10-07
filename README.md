# 🚗 MobileAppCar

## Описание проекта

MobileAppCar — это *мобильное приложение*, разработанное на Kotlin с использованием *Jetpack Compose* и платформы Supabase.
Оно позволяет пользователю искать автомобили по различным параметрам.

> “Лучший код — это тот, который сам себя объясняет.”

---

## Основные возможности

Приложение умеет:

1. Искать автомобили по *названию* и *производителю*
2. Отображать полный список машин
3. Работать с базой данных Supabase
4. Асинхронно загружать результаты
5. Обрабатывать ошибки при подключении к серверу

Пример структуры проекта:

* data/ — работа с API
* ui/ — элементы интерфейса
* viewmodel/ — логика приложения

  * Поддержка *LiveData*
  * Обработка исключений

---

## Используемые технологии

* Kotlin
* *Jetpack Compose*
* Supabase
* Ktor
* *Coroutines*

---

## Пример кода
```
fun searchCars(query: String) {
    viewModelScope.launch {
        try {
            val filter = if (query.isNotEmpty()) {
                "?or=(Manufacturer.ilike.%$query%,Name.ilike.%$query%)"
            } else ""
            
            val response = supabase.from("car_data")
                .select()
                .execute(filter)
            
            val result = response.decodeList<Car>()
            _cars.value = result

        } catch (e: Exception) {
            Log.e("CarViewModel", "Ошибка поиска", e)
        }
    }
}
```
---

##  Интерфейс приложения

![Главный экран](https://github.com/username/CarFinderApp/raw/main/images/main_screen.png)

---

## Прогресс разработки

* [x] Реализован поиск
* [x] Настроена база данных
* [x] Добавлен интерфейс
* [x] Авторизация
* [ ] Система избранного

---

## Полезные ссылки

* [Документация Supabase](https://supabase.com/docs)
* [Ktor — официальный сайт](https://ktor.io)
* [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

## Таблица компонентов

| Компонент      | Назначение    | Пример                          |
| -------------- | ------------- | ------------------------------- |
| Supabase   | Работа с БД   | .from("car_data").select()    |
| Ktor       | HTTP-запросы  | client.get("...")             |
| Compose    | Интерфейс     | LazyColumn { items(...) }     |
| Coroutines | Асинхронность | viewModelScope.launch { ... } |

---

## Заключение

MobileAppCar — это не просто приложение, а *удобный инструмент* для быстрого поиска машин.
В проекте используется современный стек технологий, а код написан *понятно и логично*.
Никаких сложных библиотек — только базовый функционал.
При этом структура кода проста, но гибка, что делает приложение легко расширяемым.

---
