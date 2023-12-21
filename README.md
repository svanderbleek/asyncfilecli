## Async File CLI

Allows "payments" between two terminal sessions on the same machine using files for communication.

## Build

```
kotlinc asyncfilecli.kt -include-runtime -d asycnfilecli.jar
```

## Run

```
java -jar asycfilecli.jar your-name partner-name
```

Then in another terminal session run

```
java -jar asycfilecli.jar partner-name user-name
```

tmux can be used to display side by side

## Use

```
> balance - display balance
0

> pay 10 - pay partner
Sent

> exit - exit
Goodbye.
```

When running the CLI and a partner pays you you should see an event

```
You were paid 10!
```

