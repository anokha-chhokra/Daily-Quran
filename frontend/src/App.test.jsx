import { render, screen, waitFor } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import App from './App'
import * as healthApi from './api/health'

describe('App', () => {
  it('shows connected once the backend health check succeeds', async () => {
    vi.spyOn(healthApi, 'checkHealth').mockResolvedValue({ status: 'UP' })

    render(<App />)

    expect(screen.getByTestId('backend-status')).toHaveTextContent('checking')
    await waitFor(() =>
      expect(screen.getByTestId('backend-status')).toHaveTextContent('connected')
    )
  })

  it('shows unreachable when the health check fails', async () => {
    vi.spyOn(healthApi, 'checkHealth').mockRejectedValue(new Error('down'))

    render(<App />)

    await waitFor(() =>
      expect(screen.getByTestId('backend-status')).toHaveTextContent('unreachable')
    )
  })
})