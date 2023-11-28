# Validators Playground

This codebase is a playground for validators. 
It is a simple web application that allows you to test different
validation libraries in an _almost_ real-life project.

## Credits

Huge thank you to my employer, [finmid](https://finmid.com)
and their Tech Weekly Talks.
They hire sustainably, so when they do - you better apply!

Kudos to an anonymous interviewee who questioned the sense of
having anything other than Jakarta Bean Validation.

People who built all those open-source libraries I'm using here.

## The Goal

The goal is to validate several invalid payloads and not crash
the application because of not valid input. 
Also, I wanted to compare multiple approaches to validation:
declarative, imperative, and functional.

Coming from a Ruby background, I wanted to see how JVM validations compare
to the Ruby ones. The benchmark is Grape API with its built-in validations. 
See it at [ruby_example](./ruby_example).

## Implementation

Run the tests with:
```bash
./gradlew check
```

Libraries covered so far:

| Library              | Website                             |
|----------------------|-------------------------------------|
| Java Bean Validation | https://beanvalidation.org/         |
| Tribune              | https://github.com/sksamuel/tribune |

## Tech Talk

I did a small introduction to the topic. See it at [tech_talk](./tech_talk).

You can run it with [patat](https://github.com/jaspervdj/patat)
or any other markdown presentation tool:
```bash
patat tech_talk/complex_validations.patat.md
```

## License

MIT License

Copyright (c) 2023 Denys Yahofarov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
