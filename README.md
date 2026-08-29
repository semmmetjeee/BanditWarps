# BanditWarps

Een uitgebreide, volledig configureerbare player-warps plugin voor Paper 26.2 en Java 25.

## Functies

- Professionele browser met paginering en vier sorteermethodes
- Openbare en privéwarps
- Echte edit-sessies: opslaan bevestigt, annuleren gooit wijzigingen weg
- Naam- en korte éénregelige beschrijvingsinvoer via Paper's native Minecraft Dialog API
- Configureerbare iconen met permissies en custom model data
- Veilige menu's met interne actie-ID's; items kunnen niet worden meegenomen
- Persistente unieke bezoekerstelling met configureerbare cooldown
- Maker, aanmaakdatum, locatie, status en bezoekers als placeholders
- Configureerbare submenu-woorden, berichten, validatie, limieten, slots, borders, items en lore
- Admin reload en beheer van andermans warps
- Automatische Maven-zip en downloadbaar GitHub Actions-artifact

## Build

```bash
mvn clean verify
```

Output:

- `target/BanditWarps.jar`
- `target/BanditWarps-1.1.2.zip`

## Commando's

- `/pwarp` – open de warpbrowser
- `/pwarp create <naam>`
- `/pwarp go <warp>`
- `/pwarp edit <warp>`
- `/pwarp delete <warp>`
- `/pwarp list`
- `/pwarp admin reload`

Subcommandwoorden zijn aanpasbaar in `commands.yml`.

## Belangrijke permissies

- `banditwarps.use`
- `banditwarps.create`
- `banditwarps.admin`
- `banditwarps.limit.<aantal>`
- `banditwarps.icon.<icoon>`

Nieuwe iconen voeg je toe aan `icons.yml`. De warpnaam en algemene warplore blijven centraal in `menus.yml`.
