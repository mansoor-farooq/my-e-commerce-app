---
name: Fresh Organic Modern Commerce
colors:
  surface: '#f7faf8'
  surface-dim: '#d7dbd9'
  surface-bright: '#f7faf8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f4f2'
  surface-container: '#ebefed'
  surface-container-high: '#e6e9e7'
  surface-container-highest: '#e0e3e1'
  on-surface: '#181c1b'
  on-surface-variant: '#404944'
  inverse-surface: '#2d3130'
  inverse-on-surface: '#eef1ef'
  outline: '#707974'
  outline-variant: '#bfc9c3'
  surface-tint: '#306855'
  primary: '#003426'
  on-primary: '#ffffff'
  primary-container: '#0f4c3a'
  on-primary-container: '#82bba4'
  inverse-primary: '#99d3ba'
  secondary: '#006c49'
  on-secondary: '#ffffff'
  secondary-container: '#6cf8bb'
  on-secondary-container: '#00714d'
  tertiary: '#600017'
  on-tertiary: '#ffffff'
  tertiary-container: '#8a0025'
  on-tertiary-container: '#ff8f96'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#b4efd6'
  primary-fixed-dim: '#99d3ba'
  on-primary-fixed: '#002117'
  on-primary-fixed-variant: '#15503e'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffdada'
  tertiary-fixed-dim: '#ffb3b6'
  on-tertiary-fixed: '#40000c'
  on-tertiary-fixed-variant: '#920028'
  background: '#f7faf8'
  on-background: '#181c1b'
  surface-variant: '#e0e3e1'
  surface-white: '#FFFFFF'
  surface-subtle-mint: '#E8F5E9'
  surface-stepper: '#F8FAFC'
  border-card: '#E2E8F0'
  border-subtle: '#EDF2F7'
  text-primary: '#0F291E'
  text-secondary: '#64748B'
  text-tertiary: '#94A3B8'
typography:
  display-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '800'
    lineHeight: 36px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '700'
    lineHeight: 24px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 10px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.05em
  price-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '800'
    lineHeight: 24px
  price-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '700'
    lineHeight: 20px
  price-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 13px
    fontWeight: '700'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1rem
  space-xl: 1.5rem
---

## Brand & Style

The design system establishes a premium, hygienic, and dependable retail environment for fast-moving consumer goods and household groceries. Balancing the warmth of organic nutrition with the brisk efficiency of modern e-commerce, the experience evokes freshness, transparency, and confidence.

### Style Archetype: Organic Modernism
Drawing inspiration from contemporary Nordic minimalism paired with modern high-end grocery standards, the interface utilizes:
- **Quiet Canvas:** A pale sage neutral backdrop (`#F4F7F5`) that eliminates aggressive digital glare, softens eye fatigue during basket building, and elevates product photography.
- **Deep Botanical Anchors:** Forest emerald (`#0F4C3A`) utilized for primary conversion buttons, navigational focal points, and core headlines.
- **Tactile Soft Surfaces:** Crisp white surfaces (`#FFFFFF`) framed with whisper-soft borders and gentle diffuse shadows, yielding structured physical distinction without heavy visual weight.
- **Restrained Signals:** Deliberate reserve with bright colors—saving high-chroma greens for active badges and subtle ruby red for sale alerts and destructive actions.

## Colors

The palette revolves around deep botanical tones and clean culinary neutrals. Dark mode is excluded entirely to preserve the crisp, daytime freshness of a premium marketplace.

### Functional Roles
- **Primary (`#0F4C3A`):** The signature deep emerald anchor. Used for primary CTA buttons (e.g., "Place order", "Continue to checkout"), prominent section headings, and active bottom navigation states.
- **Secondary (`#10B981`):** A lively accent emerald used for affirmative micro-feedback, active stock indicators, promotional badges, and cart indicator rings.
- **Tertiary (`#E11D48`):** Restrained ruby red reserved for price strike-through banners, critical discounts, remove actions (trash affordances), and inventory shortage warnings.
- **Neutral Canvas (`#F4F7F5`):** A soft, pale sage foundation that reduces blue-light glare and ensures product pack cutout images float naturally.
- **Card Surfaces (`#FFFFFF`):** High-reflectance white for all interactive cards, bottom sheets, navigation bars, and inputs.

## Typography

The typography leverages **Plus Jakarta Sans** for structural brand identity and pricing hierarchy, paired with **Inter** for data tables, specifications, and UI control microcopy.

### Currency & Price Formatting
- Currency symbol (`Rs`) and numeral values share the same baseline.
- Totals and product card prices apply `Plus Jakarta Sans` with `700` or `800` weight, set in `#0F291E` to maintain maximum legibility against light backgrounds.
- Original discount prices use `body-sm` with `line-through` styling rendered in `text-secondary` (`#64748B`).

### Hierarchy Guidelines
- **Headlines:** Use `Plus Jakarta Sans` with tight tracking (`-0.015em` to `-0.02em`) to avoid scattered letter density in mobile viewports.
- **Brand Chips:** Uppercase tags (such as brand origin indicators) use `label-sm` with `0.05em` letter spacing and bold treatment.

## Layout & Spacing

The layout model is optimized primarily for mobile touch commerce using a responsive 4-column layout on compact displays, expanding to a 12-column fixed container on tablets and web views.

### Touch Grids & Density
- **Standard Outer Canvas Margin:** `1rem` (16px) gutter padding along mobile screen edges.
- **Vertical Flow Rhythm:** Sections, category carousels, and order cards follow a disciplined `1.5rem` (24px) baseline rhythm.
- **Component Breathing Room:** Internal card padding is strictly uniform at `1rem` (16px), preventing content from crowding card borders.
- **Thumb Zones:** Primary call-to-actions are pinned to the bottom viewport with safe-area spacing and a height of `52px` to `56px` for effortless thumb reach.

## Elevation & Depth

Visual depth is achieved through tactile, low-contrast physical layers rather than dramatic, theatrical drops. The interface relies on a combination of subtle perimeter framing and diffused ambient lighting.

### Surface Tiers
- **Tier 0 (Canvas):** Soft sage base (`#F4F7F5`). Completely matte with no drop shadow.
- **Tier 1 (Cards & Product Tiles):** Solid white surface (`#FFFFFF`) with a 1px border (`#E2E8F0`) and an ambient shadow: `box-shadow: 0 4px 16px -2px rgba(15, 76, 58, 0.04), 0 2px 6px -1px rgba(0, 0, 0, 0.02)`.
- **Tier 2 (Pinned Navigation & Modal Sheets):** Floating bottom nav and checkout summary footers use `box-shadow: 0 -4px 20px 0 rgba(15, 76, 58, 0.06)` with a top border in `#EDF2F7`.
- **Tier 3 (Popovers & Toasts):** Floating alerts use `box-shadow: 0 10px 25px -3px rgba(15, 76, 58, 0.12)`.

## Shapes

The design system incorporates friendly, welcoming curved geometry that balances casual freshness with structured e-commerce discipline.

### Corner Radius System
- **Product & Content Cards:** Standardized with `1rem` (16px) to `1.25rem` (20px) corners (`rounded-lg` / `rounded-xl`).
- **Action Buttons & Steppers:** Clipped with `0.75rem` (12px) corners for crisp, tactile button press affordance.
- **Input Fields:** Styled with `0.75rem` (12px) border radii.
- **Pills & Status Indicators:** Use fully rounded pill shapes (`9999px`) for product origin tags, quantity counter badges, and active bottom navigation indicators.

## Components

### Buttons
- **Primary CTA:** Solid `#0F4C3A` with `#FFFFFF` text, `52px` min-height, `rounded-xl` (12px), bold label. Includes optional leading checkout lock icon or trailing arrow.
- **Secondary / Sign Out Button:** Transparent fill with `1.5px` border in `#0F4C3A`, text colored `#0F4C3A`.
- **Icon / Utility Buttons:** Neutral grey or soft sage circular/rounded square buttons (`40px x 40px`) with subtle borders.

### Steppers & Quantity Controls
- **Counter Capsule:** Soft, pill-shaped or rounded container (`#F8FAFC`) with subtle `#E2E8F0` border.
- **Decrement / Increment Buttons:** Squircle shapes with soft surface background (`#F1F5F9`), `32px x 32px`, centering crisp `+` and `−` symbols.
- **Delete Affordance:** Outline trash can icon in `#94A3B8`, transitioning to `#E11D48` upon hover or active swipe.

### Cards
- **Product / Cart Cards:** White surface (`#FFFFFF`), `16px` padding, `16px` border radius, bordered in `#E2E8F0`. Product images are rendered on a clean square tile (`80px x 80px` in cart, `140px x 140px` in grid) with a slight grey base (`#F8FAFC`) and `12px` rounded radius.
- **Summary Receipt Cards:** Flat `#FFFFFF` background with segmented rows for Subtotal, Delivery, and bolded Total. Totals sit within an optional soft-tinted highlight capsule (`#E8F5E9`).

### List Items & Settings Rows
- Grouped inside unified white cards with `16px` rounded corners.
- Left-aligned icon placed inside a tinted sage rounded container (`36px x 36px`).
- Right-aligned directional navigation arrow (`#64748B`).
- Separated by `1px` inner divider lines (`#EDF2F7`) inset past the icon margin.

### Bottom Navigation
- Anchored to bottom with safe-area spacing, pure `#FFFFFF` background, and soft top shadow.
- Strictly three tabs: **Shop**, **Cart**, and **Account**.
- The active tab displays an emerald pill highlight behind the icon with saturated emerald label text, while inactive tabs remain muted `#64748B`.
- The Cart tab features a prominent pill badge displaying live item counts.