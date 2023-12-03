## Trustline CLI

Allows payments between two terminal sessions on the same machine using files for communication.

## Build

```
kotlinc trustline.kt -include-runtime -d trustline.jar
```

## Run

```
java -jar trustline.jar your-name partner-name
```

Then in another terminal session run

```
java -jar trustline.jar partner-name user-name
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

When running the CLI and a parnter pays you you should see an event

```
You were paid 10!
```

