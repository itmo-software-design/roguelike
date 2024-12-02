# System Design. Roguelike

## Состав команды

* Дробышев Максим
* Кашин Георгий
* Шадрин Михаил

___

## Задачи

- [x] [Часть 1. Архитектурное описание](https://github.com/itmo-software-design/roguelike/pull/1)
- [ ] [Часть 2. Реализация прототипа](https://github.com/itmo-software-design/roguelike/pull/2)

___

## Архитектура

Подробное описание системы доступно в [разделе с документацией](docs).
___

## Легкий старт

### Автоматическая генерация PlantUML диаграмм

Чтобы поддерживать изображения схем в актуальном состоянии, написан
скрипт [generate_png_for_schemas.sh](generate_png_for_schemas.sh).
Его можно добавить в `.git/hooks/pre-commit` файл или вызывать готовую конфигурацию
запуска [Save_PlantUML_as_PNG.xml](.idea/runConfigurations/Save_PlantUML_as_PNG.xml) в Intellij IDEA.

Для корректной работы необходимо локально установить [PlantUML](https://plantuml.com/ru/starting):

* Для macOS самый быстрый способ:
    ```commandline
    brew install plantuml
    ```
* для остальных ОС подробная инструкция доступна по ссылке выше
