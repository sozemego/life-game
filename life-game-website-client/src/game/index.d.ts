export interface GameService {
    start(): Promise<any>
}

export interface GameClient {
    connect(): Promise<any>
    send(body: object): void
    authorize(): void
    requestGameWorld(): void
    onMessage(type: string, fn: () => void): () => void
}