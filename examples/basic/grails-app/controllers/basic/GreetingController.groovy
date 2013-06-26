package basic

class GreetingController {

    def index() { }

    def greet(String name) {
        render "Hello ${name}"
    }
}
