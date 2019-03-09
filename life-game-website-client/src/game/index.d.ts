export interface GameService {
    start(): Promise<any>
}

export interface GameClient {
    connect(): Promise<any>
    send(body: string): void
    authorize(): void
    requestGameWorld(): void
    onMessage(type: string, fn: () => void): () => void
}