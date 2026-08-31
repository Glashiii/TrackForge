import { create } from 'zustand'

type UiState = {
  isProjectCreateOpen: boolean
  isIssueCreateOpen: boolean
  isIssueDetailsOpen: boolean
  selectedIssueId: number | null
  setProjectCreateOpen: (isOpen: boolean) => void
  setIssueCreateOpen: (isOpen: boolean) => void
  openIssueDetails: (issueId: number) => void
  closeIssueDetails: () => void
}

export const useUiStore = create<UiState>((set) => ({
  isProjectCreateOpen: false,
  isIssueCreateOpen: false,
  isIssueDetailsOpen: false,
  selectedIssueId: null,
  setProjectCreateOpen: (isProjectCreateOpen) => set({ isProjectCreateOpen }),
  setIssueCreateOpen: (isIssueCreateOpen) => set({ isIssueCreateOpen }),
  openIssueDetails: (selectedIssueId) =>
    set({
      isIssueDetailsOpen: true,
      selectedIssueId,
    }),
  closeIssueDetails: () =>
    set({
      isIssueDetailsOpen: false,
      selectedIssueId: null,
    }),
}))
