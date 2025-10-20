export const DayOfWeek = {
    MONDAY: "MONDAY",
    TUESDAY: "TUESDAY",
    WEDNESDAY: "WEDNESDAY",
    THURSDAY: "THURSDAY",
    FRIDAY: "FRIDAY",
    SATURDAY: "SATURDAY",
    SUNDAY: "SUNDAY",
} as const;

export type DayOfWeek = typeof DayOfWeek[keyof typeof DayOfWeek];

export const DAY_OF_WEEK_VALUES: DayOfWeek[] = Object.values(DayOfWeek);