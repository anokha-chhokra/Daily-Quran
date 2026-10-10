import { describe, it, expect, vi, afterEach } from 'vitest'
import { checkHealth } from './health'

describe('checkHealth', () => {
  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('returns the parsed health status on success', async () => {
    global.fetch = vi.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ status: 'UP' }),
    })

    const result = await checkHealth()

    expect(fetch).toHaveBeenCalledWith('/api/health')
    expect(result).toEqual({ status: 'UP' })
  })

  it('throws when the backend responds with an error status', async () => {
    global.fetch = vi.fn().mockResolvedValue({ ok: false, status: 503 })

    await expect(checkHealth()).rejects.toThrow('503')
  })
})