# Guía de Diseño, Capas y Animaciones — Weather Land (Wear OS)

Este proyecto utiliza **Watch Face Format (WFF v2)** para Wear OS 5+ (Galaxy Watch 4, 5, 6, 7, 8 y Ultra).

## ¿Por qué NO necesitas API de OpenWeather?
Desde 2025/2026, Google Play exige que todas las carátulas nuevas en `.aab` usen **Watch Face Format (WFF)** (solo recursos XML, sin código Kotlin/Java ejecutable).
La gran ventaja de **WFF v2** es que **Wear OS entrega el clima de forma nativa** (`[WEATHER.CONDITION]`, `[WEATHER.TEMPERATURE]`, `[WEATHER.IS_DAY]`) usando el proveedor del reloj:
- **Cero configuración** para el usuario (sin API keys ni cuentas).
- **Cero gasto extra de batería** por llamadas HTTP.
- **100% compatible con Google Play** para subir el `.aab`.

---

## Arquitectura por Capas (Canvas Circular 450×450 px)

Todas las imágenes se reemplazan directamente en:
`app/src/main/res/drawable/`

> **Regla de oro para pantallas AMOLED (Galaxy Watch 4+):**
> - Usa **PNG con transparencia (RGBA)** para capas estáticas o animadas por código.
> - Para efectos complejos (lluvia con salpicaduras en el río, relámpagos ramificados, gotas resbalando en el vidrio), usa **Animated WebP (`.webp`)** en vez de GIF: tiene transparencia real (canal alfa de 8 bits), millones de colores y pesa una fracción.
> - Mantén la mitad inferior (`y = 240..450`) con tonos oscuros/profundos para que la hora (`8:45`), fecha y temperatura tengan máximo contraste y ahorren batería OLED.

---

### Capa 1 — Cielos (Fondo completo `450×450 px`, PNG)
| Archivo | Clima |
|---|---|
| `sky_clear_day.png` | Despejado / Soleado de día |
| `sky_clear_night.png` | Despejado de noche |
| `sky_cloudy_day.png` | Nublado / Parcialmente nublado de día |
| `sky_cloudy_night.png` | Nublado de noche |
| `sky_rain.png` | Lluvia ligera / normal / fuerte |
| `sky_storm.png` | Tormenta eléctrica |
| `sky_snow.png` | Nieve / Aguanieve |
| `sky_fog.png` | Niebla / Neblina |

### Capa 2 — Astros (Animados por código en `watchface.xml`, PNG transparente)
| Archivo | Tamaño recomendado | Animación por código |
|---|---|---|
| `celestial_sun.png` | `180×180 px` | Pulso suave de escala |
| `celestial_sun_rays.png` | `240×240 px` | Rotación continua suave |
| `celestial_moon.png` | `200×200 px` | Luna protagonista fija con halo |
| `celestial_stars_a.png` | `450×240 px` | Parpadeo suave (onda seno) |
| `celestial_stars_b.png` | `450×240 px` | Parpadeo desfasado (onda coseno) |

### Capa 3 — Nubes (Animadas por código con desplazamiento horizontal, PNG transparente)
| Archivo | Tamaño recomendado | Animación por código |
|---|---|---|
| `clouds_light.png` | `530×190 px` | Deriva lateral suave |
| `clouds_heavy.png` | `530×200 px` | Deriva lateral continua |
| `clouds_storm.png` | `530×200 px` | Deriva más intensa |

### Capa 4 — Paisaje: Montañas, Río y Pinos (`450×450 px`, PNG con cielo transparente)
La zona superior (`y = 0..210`) debe ser **transparente** para dejar ver el cielo, astros y nubes detrás de las montañas.

| Archivo | Uso |
|---|---|
| `land_day.png` | Día soleado / despejado |
| `land_night.png` | Noche despejada / nublada |
| `land_cloudy.png` | Día nublado / lluvia / niebla |
| `land_storm.png` | Tormenta (silueta oscura con reflejo azul/violeta en el río) |
| `land_snow.png` | Montañas y pinos nevados |

### Capa 5 — Efectos Climáticos (PNG animado por código o Animated WebP)
Actualmente la base anima estos PNGs por código para que funcione de inmediato. Si quieres un acabado de nivel profesional, puedes enviarme **Animated WebP (12–15 fps, loop de 2–3 s, < 300 KB cada uno)** y los conectamos con `<PartAnimatedImage format="WEBP" />`:

| Archivo actual (PNG) | Tamaño | Efecto | Upgrade opcional a Animated WebP |
|---|---|---|---|
| `fx_rain_overlay.png` | `450×530 px` | Gotas de lluvia cayendo | `fx_rain_anim.webp` (lluvia + salpicaduras en el río) |
| `fx_glass_fog.png` | `450×450 px` | Condensación/empañado en bordes + gotitas en el cristal | `fx_glass_fog_anim.webp` (gotas resbalando lentamente) |
| `fx_lightning_bolt.png` | `300×230 px` | Relámpago protagonista + destello en toda la pantalla | `fx_lightning_anim.webp` (rayo ramificado con brillo neón) |
| `fx_snow_overlay.png` | `490×530 px` | Copos cayendo con balanceo | `fx_snow_anim.webp` (copos en 2 planos de profundidad) |
| `fx_fog_mist.png` | `510×220 px` | Bancos de niebla deslizándose | PNG con animación de código (ya queda muy fluido) |
| `fx_wind_streaks.png` | `570×200 px` | Ráfagas de viento | `fx_wind_anim.webp` |

---

## Cómo exportar el `.aab` para Wear OS / Google Play
1. Abre esta carpeta (`weather_land`) en **Android Studio**.
2. Para probar en emulador Wear OS 5+ o Galaxy Watch 4+: **Run > Run 'app'**.
3. Para generar el `.aab` firmado para Play Store: **Build > Generate Signed App Bundle / APK > Android App Bundle**.
   (O desde terminal con Gradle: `gradlew bundleRelease`).
