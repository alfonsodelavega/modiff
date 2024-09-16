# Modiff (+Munidiff transformations)

This repository contains the code of the `modiff` model comparison tool. It also contains the transformations and formatters to generate textual and graphical (based on Plantuml) difference reports.

## How to run `modiff`'s backend locally (for development)

Modiff is used as a backend by the [Munidiff Timeline Explorer](https://github.com/munidiff/munidiff.github.io). This backend is implemented and deployed through Google Cloud Functions, but can be run locally via:

```bash
mvn function:run
```
