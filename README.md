## Payments CLI

Allows "payments" between two terminal sessions on the same machine using files for communication.

## Build

```
kotlinc payments.kt -include-runtime -d payments.jar
```

## Run

```
java -jar payments.jar your-name partner-name
```

Then in another terminal session run

```
java -jar payments.jar partner-name user-name
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

