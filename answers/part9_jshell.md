# Часть 9 — Эксперименты в jshell

## Как запустить jshell

Откройте терминал IntelliJ (View → Tool Windows → Terminal) и введите:
```
jshell
```
Для выхода: `/exit`

---

## Задание 9.1: Sealed-классы

### Команды (скопируйте и вставьте в jshell)

```
sealed interface Shape permits Circle, Square {}
record Circle(double r) implements Shape {}
record Square(double side) implements Shape {}
Shape s = new Circle(5)
s instanceof Circle c ? "Круг r=" + c.r() : "Не круг"
```

### Фактический вывод:

```
jshell> sealed interface Shape permits Circle, Square {}
|  created interface Shape

jshell> record Circle(double r) implements Shape {}
|  created record Circle

jshell> record Square(double side) implements Shape {}
|  created record Square

jshell> Shape s = new Circle(5)
s ==> Circle[r=5.0]

jshell> s instanceof Circle c ? "Круг r=" + c.r() : "Не круг"
$5 ==> "Круг r=5.0"
```

### Вопрос: Что произойдёт при попытке создать `record Triangle(double a) implements Shape {}`?

**Ваш ответ:** Будет ошибка компиляции.
Поскольку интерфейс Shape помечен как sealed и разрешает (permits) реализацию только для классов Circle и Square, компилятор не пропустит объявление record Triangle implements Shape. Класс Triangle отсутствует в белом списке permits, а значит, не имеет права реализовывать этот запечатанный интерфейс.





---

## Задание 9.2: Цепочка лямбд

### Команды

```
import java.util.function.*
Function<String, String> trim = String::trim
Function<String, String> upper = String::toUpperCase
Function<String, String> exclaim = s -> s + "!"
var pipeline1 = trim.andThen(upper).andThen(exclaim)
var pipeline2 = exclaim.compose(upper).compose(trim)
pipeline1.apply("  hello world  ")
pipeline2.apply("  hello world  ")
```

### Фактический вывод:

```
jshell> pipeline1.apply("  hello world  ")
$7 ==> "HELLO WORLD!"

jshell> pipeline2.apply("  hello world  ")
$8 ==> "HELLO WORLD!"
```

### Вопрос: Дают ли `andThen()` и `compose()` одинаковый результат? В каком случае результаты будут различаться?

**Ваш ответ:** Нет, в данном примере результат одинаковый, но в общем случае andThen() и compose() дают разный порядок применения функций, если только функции не являются коммутирующими.



---

## Задание 9.3: Сравнение EnumSet и HashSet.

### Команды

```
enum Color { RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, WHITE, BLACK }
var enumSet = java.util.EnumSet.of(Color.RED, Color.GREEN, Color.BLUE)
var hashSet = new java.util.HashSet<>(java.util.Set.of(Color.RED, Color.GREEN, Color.BLUE))
enumSet.contains(Color.RED)
hashSet.contains(Color.RED)
enumSet.getClass().getSimpleName()
hashSet.getClass().getSimpleName()
```

### Фактический вывод:

```
jshell> enumSet ==> [RED, GREEN, BLUE]
jshell> hashSet ==> [BLUE, GREEN, RED]
jshell> enumSet.contains(Color.RED) ==> true
jshell> hashSet.contains(Color.RED) ==> true
jshell> enumSet.getClass().getSimpleName() ==> "RegularEnumSet"
jshell> hashSet.getClass().getSimpleName() ==> "HashSet"
```

### Вопрос: Почему внутренний класс EnumSet называется `RegularEnumSet`? Что произойдёт, если enum будет иметь больше 64 констант?

**Ваш ответ:** RegularEnumSet получил такое название, потому что он рассчитан на обычный случай — когда в перечислении не более 64 элементов. Внутри он хранит всего одно поле типа long, где каждый бит отвечает за одну константу enum (бит под номером ordinal()). Благодаря этому проверка contains, добавление и удаление выполняются через быстрые побитовые операции (&, |, ^) за O(1).
Если же в enum 65 или больше констант, то один long уже не помещает все биты. В этом случае EnumSet автоматически переключается на реализацию JumboEnumSet — она использует массив long[], где каждый элемент массива покрывает 64 константы. Операции остаются эффективными, но требуют дополнительного вычисления индекса в массиве. Разработчику не нужно ничего менять в коде — выбор реализации происходит автоматически внутри фабричных методов EnumSet.

