---
name: Vitality Material
colors:
  surface: '#f9f9f9'
  surface-dim: '#dadada'
  surface-bright: '#f9f9f9'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f3f3'
  surface-container: '#eeeeee'
  surface-container-high: '#e8e8e8'
  surface-container-highest: '#e2e2e2'
  on-surface: '#1a1c1c'
  on-surface-variant: '#3f4a3c'
  inverse-surface: '#2f3131'
  inverse-on-surface: '#f0f1f1'
  outline: '#6f7a6b'
  outline-variant: '#becab9'
  surface-tint: '#006e1c'
  primary: '#006e1c'
  on-primary: '#ffffff'
  primary-container: '#4caf50'
  on-primary-container: '#003c0b'
  inverse-primary: '#78dc77'
  secondary: '#9f4122'
  on-secondary: '#ffffff'
  secondary-container: '#fd8863'
  on-secondary-container: '#722104'
  tertiary: '#006494'
  on-tertiary: '#ffffff'
  tertiary-container: '#5ba2d5'
  on-tertiary-container: '#003653'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#94f990'
  primary-fixed-dim: '#78dc77'
  on-primary-fixed: '#002204'
  on-primary-fixed-variant: '#005313'
  secondary-fixed: '#ffdbd0'
  secondary-fixed-dim: '#ffb59e'
  on-secondary-fixed: '#3a0b00'
  on-secondary-fixed-variant: '#7f2a0d'
  tertiary-fixed: '#cbe6ff'
  tertiary-fixed-dim: '#8ecdff'
  on-tertiary-fixed: '#001e30'
  on-tertiary-fixed-variant: '#004b71'
  background: '#f9f9f9'
  on-background: '#1a1c1c'
  surface-variant: '#e2e2e2'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 57px
    fontWeight: '700'
    lineHeight: 64px
    letterSpacing: -0.25px
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
  title-lg:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '500'
    lineHeight: 28px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0.5px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0.25px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.1px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.5px
  stat-lg:
    fontFamily: Inter
    fontSize: 45px
    fontWeight: '700'
    lineHeight: 52px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  margin-mobile: 16px
  margin-tablet: 24px
  gutter: 16px
  stack-sm: 8px
  stack-md: 16px
  stack-lg: 24px
---

## Brand & Style

The design system is rooted in the **Material 3 (M3)** philosophy, tailored specifically for the health and wellness space. The brand personality is encouraging, dependable, and highly legible, aiming to reduce the cognitive load of calorie tracking. 

The aesthetic follows a **Modern Corporate** style with a focus on high utility and an "Android-native" soul. It leverages generous whitespace to prevent the UI from feeling cluttered with data, ensuring that the user's health journey feels spacious and manageable. The emotional response is one of calm control and optimistic progress.

## Colors

The palette is designed for high accessibility and clear functional mapping. 

- **Primary (Wellness Green):** Used for primary actions, health goal completion, and positive reinforcement.
- **Secondary (Energy Coral):** Reserved for calorie-related data, metabolic tracking, and active energy expenditure.
- **Surface & Background:** An off-white base (#FAFAFA) reduces screen glare and distinguishes card elements from the background.
- **On-Surface/Text:** High-contrast dark charcoal for primary content, transitioning to a medium-light gray for metadata and secondary labels to maintain a clear visual hierarchy.

## Typography

This design system utilizes **Inter** for its exceptional legibility and systematic weight distribution, critical for a data-dense health app.

- **Data Hierarchy:** Key statistics (like remaining calories) use `stat-lg` or `display-lg` with a Bold weight to ensure they are the first thing a user sees.
- **Secondary Information:** Labels and support text use `label-md` in a medium-gray tone (#757575) to recede visually, allowing the numbers to pop.
- **Readability:** Line heights follow the standard 8dp grid to ensure vertical rhythm and comfortable reading during long-form content like nutritional articles.

## Layout & Spacing

This design system follows a **Fluid Grid** model optimized for the Jetpack Compose `Scaffold` and `LazyColumn` structures. 

- **The 8dp Rule:** All margins, paddings, and component heights are multiples of 8dp. 
- **Touch Targets:** Minimum touch targets for all interactive elements (buttons, chips, list items) are 48x48dp.
- **Mobile Layout:** A 4-column grid with 16dp outer margins.
- **Tablet/Large Screen:** An 8-column or 12-column grid. Content should be contained within a maximum width of 840dp to prevent awkward horizontal eye tracking on tablets.

## Elevation & Depth

In accordance with Material 3, depth is communicated through **Tonal Layers** and extremely **Soft Ambient Shadows**.

- **Level 0 (Background):** #FAFAFA.
- **Level 1 (Cards/Surface):** White (#FFFFFF) with a subtle 1px border (#EEEEEE) or a very soft shadow (Blur 8dp, Opacity 4%, Y-Offset 2dp).
- **Level 2 (Active Elements):** Used for elevated buttons or floating action buttons (FAB), utilizing the primary green with a slightly more pronounced shadow (Opacity 10%) to suggest interactability.
- **Scrims:** Use a 30% black overlay for modals and bottom sheets to maintain focus on the task at hand.

## Shapes

The shape language is "Soft-Modern." The system uses a **16px (1rem)** base corner radius for the majority of containers to evoke a friendly, approachable feel.

- **Small Components:** Checkboxes and small chips use 8px (0.5rem).
- **Medium Components:** Buttons and standard Cards use 16px (1rem).
- **Large Components:** Bottom sheets and large modal containers use 24px (1.5rem) on top corners.

## Components

### Buttons
- **Primary:** Filled button with `primary_color_hex`, white text, 16px radius.
- **Secondary (Tonal):** A soft version of the energy coral or green with 12% opacity background and full-color text.

### Cards & Progress
- **Health Cards:** Use Level 1 elevation. Include a 4px tall linear progress bar for daily goals.
- **Ring Indicators:** Use `secondary_color_hex` for "calories consumed" and a neutral gray for "remaining."

### Inputs & Selection
- **Text Fields:** Outlined Material 3 style. The border remains neutral until focused, where it transitions to the primary wellness green.
- **Chips:** Highly rounded (pill-shaped) for food categories (e.g., "High Protein," "Vegan").

### Specialized Components
- **The FAB:** A large, rounded-square (16px) floating action button for "Quick Add Food," consistently anchored at the bottom right.
- **List Items:** 72dp minimum height for food entries, including a thumbnail (40x40dp, 8px radius) and chevron-right indicator.