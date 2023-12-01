## Trustline CLI

Truevault engineering challenge, see: https://gist.github.com/traviscrist/c5b3ff8e52536917556f89727e2c9b10

## Build

```
kotlinc trustline.kt -include-runtime -d trustline.jar
```

## Run

```
java -jar trustline.jar your-name partner-name
```

## Use

```
> balance - display balance
0

> pay 10 - pay partner
Sent

> exit - exit
Goodbye.
```

When running the CLI and a parnter pays you you should see an event

```
You were paid 10!
```
