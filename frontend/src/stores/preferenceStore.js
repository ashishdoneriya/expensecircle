import { defineStore } from "pinia";

import { useStorage } from '@vueuse/core'

export const usePreferenceStore = defineStore('preferences', {
  state: () => ({
    // This whole state is now synced with localStorage
    sortOrder: useStorage('expensesSortOrder', 'DateOldestToNewest')
  })
})
