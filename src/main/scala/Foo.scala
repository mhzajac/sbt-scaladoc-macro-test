
/** This is a class that will have some methods copied. */
@expand class Foo {
    /** Remove this scaladoc comment, and `sbt doc` will run just fine! */
    def bar(value: String) = value
}

/** Another class. */
class Fizz(foo: Foo) {
    /** More scaladoc, nothing wrong here. */
    def str = foo.barCopy("hey")
}
